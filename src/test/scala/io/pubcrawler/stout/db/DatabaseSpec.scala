package io.pubcrawler.stout.db

import com.spotify.docker.client.exceptions.DockerRequestException
import com.spotify.docker.client.messages.HostConfig.Bind
import com.spotify.docker.client.messages.{ContainerConfig, HostConfig, PortBinding}
import com.spotify.docker.client.{DefaultDockerClient, DockerClient}
import com.typesafe.config.ConfigValueFactory
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

import scala.collection.JavaConverters._


class DatabaseSpec extends FlatSpec with Matchers with BeforeAndAfter with Database {

  private val docker: DockerClient = DefaultDockerClient.fromEnv().build()
  private var id: Option[String] = None

  before {
    docker.pull("postgres:9.6.2")

    val hostConfig = HostConfig.builder()
      .appendBinds(Bind.from(getClass.getResource("/db/migration").getPath)
        .to("/docker-entrypoint-initdb.d")
        .readOnly(true)
        .build)
      .portBindings(Map("5432" -> List(PortBinding.randomPort("127.0.0.1")).asJava).asJava)
      .build

    val containerConfig = ContainerConfig.builder()
      .hostConfig(hostConfig)
      .image("postgres:9.6.2")
      .env(List("POSTGRES_PASSWORD=pass").asJava)
      .exposedPorts("5432")
      .attachStdout(true)
      .attachStderr(true)
      .build

    id = Some(docker.createContainer(containerConfig).id())

    try {
      docker.startContainer(id.get)
      val (_, list) = docker.inspectContainer(id.get).networkSettings().ports().asScala.head
      val portBinding = list.asScala.head
      this.config = config.withValue("postgres.url",
        ConfigValueFactory.fromAnyRef(s"jdbc:postgres://${portBinding.hostIp()}:${portBinding.hostPort()}/postgres"))
    } catch {
      case e: DockerRequestException => println(e.getMessage)
    }
  }

  after{
    docker.killContainer(id.get)
    docker.removeContainer(id.get)
    docker.close()
  }

  it should "be running" in {
    assert(docker.inspectContainer(id.get).state().status() == "running")
    info(s"Postgres docker database available at ${this.config.getValue("postgres.url").unwrapped()}")
  }
}

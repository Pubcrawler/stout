package io.pubcrawler.stout


class PubcrawlController extends StoutStack {

  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
      </body>
    </html>
  }

}

package io.pubcrawler.stout

import org.scalatra._

class PubcrawlController extends StoutStack {

  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
      </body>
    </html>
  }

}

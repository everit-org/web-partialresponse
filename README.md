# web-partialresponse

An amazingly small, JQuery based library that can create partial-responses on the
server side and process them on the client side.

With PartialResponse, you can do all DOM manipulations from a Servlet and an
AJAX response that you have done within JQuery till now:

 - append based on CSS selector
 - prepend based on CSS selector
 - replace based on CSS selector
 - replace based on the id of the root content element

## Download

The artifact is available on [maven-central][1].

## Usage

### Usage on the server side

    public void service(ServletRequest request, ServletResponse response) {
      // Create a new partial response builder
      PartialResponseBuilder prb = new PartialResponseBuilder(response);

      // Do modifications on the website based on CSS selectors
      prb.append(".append-area", "<p>One more paragraph</p>");

      // Or replace any content based on its id
      prb.replaceById(
         "<p id=\"current-time\">" + new Date().toString() + "</p>");

      // Close the builder to write the end of the content
      // PartialResponseBuilder implements Closeable to let the programmer
      // use it within a try block.
      prb.close();
    }

### Usage on the client side

    <html>
    <head>
      <!-- Include JQuery before partialresponse is included -->
      <script src="jquery.js"></script>

      <!-- Include the partialresponse javascript file -->
      <script src="partialresponse.js"></script>
    </head>

    <body>
      <h1>Current time</h1>
      <p id="current-time">Current time will come here</p>
      
      <h1>Append area</h1>
      <div class="append-area">
      </div>
      <a onclick="doModifications(); return false;">Do modifications</a>
      
      <script>
        function doModifications() {
          $.ajax().done(function(content) {

            // Call partial response client code
            everit.partialresponse.process(content);

          });
        }
      </script>
    </body>
    </html>


[1]: http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.everit.web%22%20AND%20a%3A%22org.everit.web.partialresponse%22

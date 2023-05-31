window.onload = function () {

    var url = window.location.search.substring(1);

    if (url.length == 0) {
        url = "swagger.json";
    }

    // Build a system
    const ui = SwaggerUIBundle({
        url: url,
        dom_id: '#swagger-ui',
        deepLinking: true,
        presets: [
            SwaggerUIBundle.presets.apis,
            SwaggerUIStandalonePreset
        ],
        plugins: [
            SwaggerUIBundle.plugins.DownloadUrl
        ],
        layout: "StandaloneLayout"
    })
    window.ui = ui
}

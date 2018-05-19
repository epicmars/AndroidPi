function mozillaRead() {
    var loc = document.location;
    var uri = {
        spec: loc.href,
        host: loc.host,
        prePath: loc.protocol + "//" + loc.host,
        scheme: loc.protocol.substr(0, loc.protocol.indexOf(":")),
        pathBase: loc.protocol + "//" + loc.host + loc.pathname.substr(0, loc.pathname.lastIndexOf("/") + 1)
    };
    var article = new Readability(uri, document).parse();

    var readable = '<!DOCTYPE html><html><head><meta charset="utf-8"/><link rel="stylesheet" href="file:///android_asset/web/static/css/media-query.css"><link rel="stylesheet" href="file:///android_asset/css/readable.css"><style>img {max-width: 100%;}</style></head><body><h1>' + article.title + '</h1>' + article.content + '</body></html>'
    document.write(readable)
}

function read() {
    var innerHtml = readability.init(document, document.body);
    var readable = '<!DOCTYPE html><html><head>\
                    <meta charset="utf-8"/>\
                    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>\
                    <link rel="stylesheet" href="file:///android_asset/web/static/h5bp/css/main.css">\
                    <link rel="stylesheet" href="file:///android_asset/web/static/h5bp/css/normalize.css">\
                    <link rel="stylesheet" href="file:///android_asset/web/static/readability/css/media-query.css">\
                    <link rel="stylesheet" media="screen and (-webkit-device-pixel-ratio: 1.5)" href="file:///android_asset/web/static/readability/css/hdpi.css" />\
                    <link rel="stylesheet" media="screen and (-webkit-device-pixel-ratio: 1.0)" href="file:///android_asset/web/static/readability/css/mdpi.css" />\
                    <link rel="stylesheet" href="file:///android_asset/web/static/readability/css/readable.css" type="text/css">\
                    </head><body>' + innerHtml + '<hr><footer><div>完</div></footer></body></html>';
    return readable
}

function render() {
    var readablePage = read()
    reader.read(document.documentElement.outerHTML, readablePage);
}

// read before images are loaded
document.addEventListener("DOMContentLoaded", function (event) {
    render()
});

render()
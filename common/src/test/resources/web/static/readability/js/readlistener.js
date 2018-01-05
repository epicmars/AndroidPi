function read() {
    console.log(this + "reader: start" + document.body.textContent);
    var innerHtml = readability.init(document, document.body);
    var readable = '<!DOCTYPE html><html><head>\
                    <meta charset="utf-8"/>\
                    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>\
                    <link rel="stylesheet" href="file:///android_asset/web/static/h5bp/css/main.css">\
                    <link rel="stylesheet" href="file:///android_asset/web/static/h5bp/css/normalize.css">\
                    <link rel="stylesheet" href="file:///android_asset/web/static/readability/css/media-query.css">\
                    <link rel="stylesheet" media="screen and (-webkit-device-pixel-ratio: 1.5)" href="file:///android_asset/web/static/css/hdpi.css" />\
                    <link rel="stylesheet" media="screen and (-webkit-device-pixel-ratio: 1.0)" href="file:///android_asset/web/static/css/mdpi.css" />\
                    <link rel="stylesheet" href="file:///android_asset/web/static/readability/css/readable.css" type="text/css">\
                    </head><body>' + innerHtml + '<hr><footer><div>完</div></footer></body></html>';
    return readable;
}

var count = 1;
var loaded = false;

function render() {
    console.log(this + "reader: render" + count++)
    var readablePage = read();
    console.log(this + "reader-complete: " + readablePage);
    reader.read(readablePage);
    loaded = true;
    console.log(this + "reader: " + readablePage);
}

// read before images are loaded
document.addEventListener("DOMContentLoaded", function (event) {
    console.log(this + "reader: " + loaded + "|" + count);
    if (!loaded) {
        reader.readable();
        render();
    }
});
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Replicator Log Explorer</title>
    <link rel="icon" href="../../assets/blue-confluent.svg">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700;900&display=swap" rel="stylesheet">
    <! --link rel="preload" as="font" crossorigin="anonymous" href="https://cdn.confluent.io/fonts/MarkPro/MarkPro-NarrowBook.woff2" type="font/woff2"/ -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/codemirror/6.65.7/codemirror.min.css" integrity="sha512-uf06llspW44/LZpHzHT6qBOIVODjWtv4MxCricRxkzvopAlSWnTf6hpZTFxuuZcuNE9CBQhqE0Seu1CoRk84nQ==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <style type="text/css">
        /*
        * {
          font-family: 'Roboto', sans-serif !important;
        }*/



        /*
        @font-face {
          font-family: "MarkPro-NarrowBook", sans-serif !important;
          src: url("https://cdn.confluent.io/fonts/MarkPro/MarkPro-NarrowBook.woff2") format('woff2');
        }

        * {
          font-family: "MarkPro-NarrowBook", sans-serif !important
          src: url("https://cdn.confluent.io/fonts/MarkPro/MarkPro-NarrowBook.woff2") format('woff2');
        } */

        .navbar a {
          font-family: '"MarkPro-NarrowBook", sans-serif !important
          src: url("https://cdn.confluent.io/fonts/MarkPro/MarkPro-NarrowBook.woff2") format('woff2');
        }
        #textarea { text-align:left; }
    </style>
        <style media="screen, print">
          @font-face {
            font-family: "MarkPro-NarrowBook";
            src: url("https://cdn.confluent.io/fonts/MarkPro/MarkPro-NarrowBook.woff2");
          }


          @font-face {
            font-family: "MarkPro-Bold";
            src: url("https://cdn.confluent.io/fonts/MarkPro/MarkPro-Bold.woff2");
          }

          @font-face {
            font-family: "MarkPro-NarrowBold";
            src: url("https://cdn.confluent.io/fonts/MarkPro/MarkPro-NarrowBold.woff2");
          }

          body {
            font-family: "MarkPro-NarrowBook", sans-serif !important;
          }

          body .fw-bold {
            font-family: "MarkPro-Bold", sans-serif !important;
          }

         body .fw-semibold {
            font-family: "MarkPro-NarrowBold", sans-serif !important;
         }
        </style>
  </head>
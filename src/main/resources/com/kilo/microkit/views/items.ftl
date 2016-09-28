<html>
<head>
    <script type="text/javascript" src="/js/jquery.js"></script>
    <link rel="stylesheet" href="/css/bootstrap-theme.css">
    <link rel="stylesheet" href="/css/bootstrap.css">
    <link rel="stylesheet" href="/css/fonts.css">
    <script type="text/javascript" src="/js/bootstrap.js"></script>
    <script type="text/javascript" src="/js/jquery.tablesorter.js"></script>


    <style>
        body {
            font-family:handwriting;
        }
        .table-x {
            padding:3px;
        }
        table {
            border : solid 1px grey;
            padding:1px;
            overflow:auto;
        }
        .table-y {
            padding:1px;
        }
        td {
            overflow:auto;
        }
        .first-row {
            margin-top:5px;
        }
        .table {
            margin-top:5px;
            margin-bottom:5px;
        }
        .catg{
            background-color:lightsalmon;
            border:solid 1px lightslategrey;
        }


    </style>
    <script>

        $(document).ready(function () {
            console.log( "## Welcome to the E-Store ## ");

            $.get( 'http://localhost:8080/i/a/categories', function(x){
                for( var i in x) {
                    $('#categories').append( '<p><label class="catg">' + format(x[i]) + "</label>");
                }
            });

        });

        function format( a ) {
            return a.split('_').map( function(x) {return x.charAt(0).toUpperCase() + x.slice(1);} ).join(' ');
        }

        function a_resize() {
            $('img').css('height','100px');
        }

        function a_click(x) {
            console.log('redirecting to ' + x);
            document.location = x;
        }

    </script>

</head>
<body>

<div class="container">

    <div style="border:solid 1px #e6e6e6;margin-top:5px;"></div>

    <div class="row first-row">
        <div class="col-sm-3"></div>
        <div class="col-sm-8">
            <form class="form-inline" action="../../../i/a/s" method="POST">
                <div class="form-group">
                    <input type="text" size="45" class="form-control" id="task" name="searchTerm">
                    <button type="submit" class="btn btn-default">Search</button>
                </div>
            </form>
        </div>
        <div class="col-sm-1"></div>
    </div>

    <div style="border : solid 1px #e6e6e6;;"></div>


    <div class="row">
        <div class="col-sm-3">
                <div id="categories"></div>
        </div>
        <div class="col-sm-3">
                <#list items as item>
            <div class="table-unit">
                <table class="table table-striped" data-toggle="tooltip" title="${item.desc}">
                    <tr>
                        <td>
                            ${item.title}
                        </td>
                        <td>
                            <img src="${item.images[0]}" style="max-height: 100%; max-width: 100%" onload="javascript:a_resize()"/>
                        </td>
                    </tr>
                    <tr  style="background-color:#f9f9e9;">
                        <td>
                            <span style="font-weight:bold;" class="class="btn btn-success">
                            &#x20B9 ${item.price}
                            </span>
                        </td>
                        <td>
                            <button class="btn btn-default" onclick="javascript:a_click('${item.url}')">
                                Buy on Flipkart
                            </button>
                        </td>
                    </tr>
                </table>
                    </div>
                </#list>
        </div>
        <div class="col-sm-3">
        </div>
        <div class="col-sm-3">
        </div>
    </div>
</div>
</body>
</html>
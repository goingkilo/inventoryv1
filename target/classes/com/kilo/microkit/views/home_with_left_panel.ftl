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
        table {
            border : solid 1px grey;
            padding:1px;
            overflow:auto;
        }
        td {
            overflow:auto;
        }
        .catg{
            #background-color:lightsalmon;
            background-color:aliceblue;
            border:solid 1px lightslategrey;
            display:block;
            margin:2px;
            white-space: normal;
        }


    </style>
    <script>

        $(document).ready(function () {
            console.log( "## Welcome to the E-Store ## ");
        });

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
<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#"> Kilo Commerce Ltd</a>
        </div>
        <ul class="nav navbar-nav">
            <li class="active"><a href="#">Products</a></li>
            <li><a href="#">Deals & Offers</a></li>
            <li><a href="#">About</a></li>

        </ul>

        <ul class="nav navbar-nav navbar-right">
            <ul class="nav navbar-nav navbar-left">
                <li>
                    <form style="margin-top:5px;" class="form-inline" action="../../../i/a/s" method="POST">
                        <div class="form-group">
                            <input type="text" size="45" class="form-control" id="task" name="searchTerm">
                            <button type="submit" class="btn btn-default">Search</button>
                        </div>
                    </form>
                </li>
            </ul>
            <li><a href="#"><span class="glyphicon glyphicon-user"></span> Sign Up</a></li>
            <li><a href="#"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
        </ul>
    </div>
</nav>

<div class="container">
    <div class="row">
        <div class="col-sm-2">
        <#list categories?keys as c>
            <div class=".btn-group-vertica">
                <button type="button" class="btn btn-block catg" id="${c}">
                ${categories[c]}
                </button>
            </div>
        </#list>
        </div>
        <div class="col-sm-3">
        <#list products as p>
            <table class="table table-striped" data-toggle="tooltip" title="${p.description}">
                <tr>
                    <td>${p.title}</td>
                    <td>
                        <img src="${p.imageURL}" style="max-height: 100%; max-width: 100%" onload="javascript:a_resize()"/>
                    </td>
                </tr>
                <tr  style="background-color:#f9f9e9;">
                    <td>
                        <span style="font-weight:bold;" class="class="btn btn-success">&#x20B9 ${p.sellingPrice}</span>
                    </td>
                    <td>
                        <button class="btn btn-default" onclick="javascript:a_click('${p.productUrl}')">
                            Buy on Flipkart
                        </button>
                    </td>
                </tr>
            </table>
        </#list>
        </div>
        <div class="col-sm-3">
        <#list products as p>
            <table class="table table-striped" data-toggle="tooltip" title="${p.description}">
                <tr>
                    <td>${p.title}</td>
                    <td>
                        <img src="${p.imageURL}" style="max-height: 100%; max-width: 100%" onload="javascript:a_resize()"/>
                    </td>
                </tr>
                <tr  style="background-color:#f9f9e9;">
                    <td>
                        <span style="font-weight:bold;" class="class="btn btn-success">&#x20B9 ${p.sellingPrice}</span>
                    </td>
                    <td>
                        <button class="btn btn-default" onclick="javascript:a_click('${p.productUrl}')">
                            Buy on Flipkart
                        </button>
                    </td>
                </tr>
            </table>
        </#list>
        </div>
        <div class="col-sm-3">
        <#list products2 as p>
            <table class="table table-striped" data-toggle="tooltip" title="${p.description}">
                <tr>
                    <td>${p.title}</td>
                    <td>
                        <img src="${p.imageURL}" style="max-height: 100%; max-width: 100%" onload="javascript:a_resize()"/>
                    </td>
                </tr>
                <tr  style="background-color:#f9f9e9;">
                    <td>
                        <span style="font-weight:bold;" class="class="btn btn-success">&#x20B9 ${p.sellingPrice}</span>
                    </td>
                    <td>
                        <button class="btn btn-default" onclick="javascript:a_click('${p.productUrl}')">
                            Buy on Flipkart
                        </button>
                    </td>
                </tr>
            </table>
        </#list>
        </div>

    </div><!-- end row-->

</div> <!-- end container-->

</body>
</html>
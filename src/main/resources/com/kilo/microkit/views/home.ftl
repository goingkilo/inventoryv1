<html>
<head>
    <script type="text/javascript" src="/js/jquery.js"></script>
    <link rel="stylesheet" href="/css/bootstrap-theme.css">
    <link rel="stylesheet" href="/css/bootstrap.css">
    <link rel="stylesheet" href="/css/fonts.css">
    <script type="text/javascript" src="/js/bootstrap.js"></script>


    <style>
        body {
            font-family:handwriting;
        }
        table {
            border : solid 1px grey;
            padding:1px;
            table-layout: fixed;
            word-wrap: break-word;
        }
        }
        td {
            /*overflow:auto;*/
            word-wrap:break-word;
            word-break:break-word;
        }
        .catg{
            #background-color:lightsalmon;
            background-color:aliceblue;
            border:solid 1px lightslategrey;
            display:block;
            margin:2px;
            white-space: normal;
        }
        .item-text {

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

        function cat(x) {
            console.log('call for  ' + x);
        }
    </script>

</head>
<body>

<#-- nav bar -->
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

<#-- left panel -->

<div class="container-fluid">

    <div class="row">
        <div class="col-sm-2">
        <#list categories as c>
            <div class=".btn-group-vertica">
                <button type="button" class="btn btn-block catg" id="${c.title}" onclick="javascript:a_cat('${c.url}')">
                ${c.displayName}
                </button>
            </div>
        </#list>
        </div>

        <#-- actual grid of inventory -->
        <#--split list into chunks of 10-->
        <#list products?chunk(10) as p3>
            <div class="col-sm-3">
                <#list p3 as p>
                    <table class="table table-striped" data-toggle="tooltip" title="${p.desc}">
                        <tr>
                            <td id="item-text">${p.title}</td>
                            <td>
                                <img src="${p.image}" style="max-height: 100%; max-width: 100%" onload="javascript:a_resize()"/>
                            </td>
                        </tr>
                        <tr  style="background-color:#f9f9e9;">
                            <td>
                                <span style="font-weight:bold;" class="class="btn btn-success">&#x20B9 ${p.price}</span>
                            </td>
                            <td>
                                <button class="btn btn-default" onclick="javascript:a_click('${p.url}')">
                                    Buy on Flipkart
                                </button>
                            </td>
                        </tr>
                    </table>
                </#list>
            </div>
        </#list>

    </div><!-- end row-->

</div> <!-- end container-->

</body>
</html>
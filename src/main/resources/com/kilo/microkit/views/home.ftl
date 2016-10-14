<html>
<head>
    <script type="text/javascript" src="/static/js/jquery.js"></script>
    <link rel="stylesheet" href="/static/css/bootstrap-theme.css">
    <link rel="stylesheet" href="/static/css/bootstrap.css">
    <link rel="stylesheet" href="/static/css/fonts.css">
    <script type="text/javascript" src="/static/js/bootstrap.js"></script>


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
        .filter {
            border :solid 1px grey;
            border-radius: 5px;
            padding:1px;
            margin-left:3px;
        }

    </style>

    <script>

        $(document).ready(function () {
            console.log( "## Welcome to the E-Store ## ");
        });

        function go_home() {
            document.location = '/i/a';
        }

        function a_resize() {
            $('img').css('height','100px');
        }

        function a_click(x) {
            console.log('redirecting to ' + x);
            window.open( x);
        }

        function a_sortby(x) {
            console.log('redirecting to ' + x);
            if( $('#sel_cat').val() != undefined) {
                var catg = $('#sel_cat').val();
                document.location = './?sort=' + x + '&category=' + catg;
                console.log('redirecting to ' + x + "/" + catg);
            }
            else {
                document.location = './?sort=' + x ;
            }
        }

        function get_category(x) {
            console.log('call for  ' + x);
            document.location = './?category=' + x;
        }
    </script>
    <script>
        (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
                    (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
                m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
        })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

        ga('create', 'UA-67365759-1', 'auto');
        ga('send', 'pageview');

    </script>
</head>
<body>

<#-- nav bar -->
<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">5 Kilo Commerce Ltd</a>
        </div>
        <ul class="nav navbar-nav">
            <li class="active"><a href="#" onclick="javascript:go_home()">Products</a></li>
            <li><a href="#" >Deals & Offers</a></li>
            <#--<li><a href="#">About</a></li>-->

        </ul>

        <ul class="nav navbar-nav navbar-right">
            <ul class="nav navbar-nav navbar-left">
                <li>
                    <form style="margin-top:5px;" class="form-inline" action="/" method="POST">
                        <div class="form-group">
                            <input type="text" size="45" class="form-control" id="task" name="searchTerm">
                            <button type="submit" class="btn btn-default">Search</button>
                        </div>
                    </form>
                </li>
                <li>&nonbreakingspace;</li>
            </ul>
            <#--<li><a href="#"><span class="glyphicon glyphicon-user"></span> Sign Up</a></li>-->
            <#--<li><a href="#"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>-->
        </ul>
    </div>

</nav>

<#-- left panel -->

<div class="container-fluid">

    <div class="row">

        <div class="col-sm-2">
        <#list categories as c>
            <div class=".btn-group-vertica">
                <button type="button" class="btn btn-block catg" id="${c.title}" onclick="javascript:get_category('${c.title}')">
                ${c.displayName}
                </button>
            </div>
        </#list>
        </div>

        <div class="col-sm-9">
            <div class="row">
                <div class="col-sm-1">
                </div>
                <div class="col-sm-11">
                    <label>Sort by</label>

                    <a href="#" class="filter" onclick="javascript:a_sortby('brand')">Brand</a>
                    <a href="#" class="filter" onclick="javascript:a_sortby('pricel')">Price - low to high</a>
                    <a href="#" class="filter" onclick="javascript:a_sortby('priceh')">Price - high to low</a>
                    <div style="margin-bottom:1px;border:solid 1px #e6e6e6;margin:3px;"></div>

                </div>
            </div>

            <#list products?chunk(3) as p3>

            <div class="row">
                <#list p3 as p>
                <div class="col-sm-4">
                    <table class="table table-striped" data-toggle="tooltip" title="${p.desc}">
                        <tr>
                            <input type="hidden" id="sel_cat" value="${p.category}"/>
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
                </div>
                </#list>
            </div>
            </#list>
        </div>

    </div><!-- end row-->

    <div class="col-sm-1">
    </div>



</div> <!-- end container-->

</body>
</html>
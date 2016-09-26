<html>
<head>
    <script type="text/javascript" src="/js/jquery.js"></script>
    <link rel="stylesheet" href="/css/bootstrap-theme.css">
    <link rel="stylesheet" href="/css/bootstrap.css">
    <#--<link rel="stylesheet" href="/css/fonts.css">-->
    <script type="text/javascript" src="/js/bootstrap.js"></script>
    <script type="text/javascript" src="/js/jquery.tablesorter.js"></script>


    <style>

        h1 {
            font-family: typewriter;
        }

        body {
            font-family: typewriter;
        }

        form {
            display: inline-block;
        }

        .form-group {
            text-align: center;
            padding-bottom: 10px;
            padding-top: 10px;
        }

        li {
            border-bottom: solid 1px black;
        }

        li:hover {
            background-color: #B6CDE5;
        }

        .first-row {
            margin-top: 4px;
        }

        .cols-sm-3 {
            border:solid 1px black;
        }
        #my_date {
            float:right;
        }

        .table-x {
            border : solid 1px grey;
            padding:15px;
        }
        .table-y {
            padding:15px;
        }
        td {
            overflow:hidden;
        }

    </style>
    <script>

        function get_date() {
            var month_names = ["January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
            ];
            var today = new Date();
            var dd = today.getDate();
            var mm = month_names[ today.getMonth()];
            var yyyy = today.getFullYear();
            if( mm < 10 ){
                mm='0' + mm
            }
            var today = dd + ' ' + mm + ' ' + yyyy;
            return today;
        }

        $(document).ready(function () {
            console.log('ready');
            $('#my_date').text( get_date() );

            $('li').click(function(e){
                console.log(e.target.id)});
        });

    </script>

</head>
<body>

<div class="container">

    <div style="border:solid 1px #e6e6e6;"></div>

    <div class="row first-row">
        <div class="col-sm-3"></div>
        <div class="col-sm-8">
            <form class="form-inline" action="../../i/a/s1" method="POST">
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
        <div class="col-sm-4">
            1
        </div>
        <div class="col-sm-4">
            <#list items as item>
            <div class="table-y">
                <div class="table-x">
            <table class="tabe table-striped">
                <tr>
                    <td>
                       <h5>
                           <div style="overflow:hidden">
                                ${item.title}
                           </div>
                       </h5>
                    </td>
                    <td>
                        <span style="font-weight:bold;" class="class="btn btn-success">
                        &#x20B9 ${item.price}
                        </span>
                    </td>

                </tr>
                <tr>
                    <div>
                        <td>
                            <img src="${item.images[0]}" style="width:100px;height:100px"></img>
                        </td>
                        <td>
                            <button class="btn btn-default">
                                <a href="${item.url}">See on Flipkart</a>
                            </button>
                        </td>
                    </div>
                </tr>
                <tr>
                </tr>
            </table>
                </div>
            </div>
            </#list>

        </div>
        <div class="col-sm-2">
        3

        </div>
        <div class="col-sm-2">
        4
        </div>
    </div>
</div>
</body>
</html>
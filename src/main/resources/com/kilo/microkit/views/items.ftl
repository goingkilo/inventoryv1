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
            padding-bottom: 25px;

        }

        li {
            border-bottom: solid 1px black;
        }

        li:hover {
            background-color: #B6CDE5;
        }

        .first-row {
            background: #e6e6e6;
            margin-top: 4px;
            border-radius: 25px;
        }

        .cols-sm-3 {
            border:solid 1px black;
        }
        #my_date {
            float:right;
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

    <div class="row first-row">
        <div class="col-sm-2"></div>
        <div class="col-sm-8">
            <form class="form-inline" action="/api/items/new" method="POST">
                <div class="form-group">
                    <label for="exampleInputName2">
                        <h3>To-Do
                            <small>List</small>
                        </h3>
                    </label>
                    <input type="text" size="45" class="form-control" id="task" name="data"
                           placeholder="world domination, get milk">
                    <input type="hidden" class="form-control" id="owner" name="owner" value="wumen ekai">
                    <button type="submit" class="btn btn-default">Note to self</button>
                </div>

            </form>
        </div>
        <div class="col-sm-2"></div>
    </div>

    <div style="border : solid 1px #e6e6e6;;"></div>

    <div>
        goingkilo@gmail.com
        <div id='my_date'></div>
    </div>

    <div style="border : solid 1px #e6e6e6;;"></div>

    <div class="row">
        <div class="col-sm-3">
        </div>
        <div class="col-sm-3">

            <#list items as item>
            <table>
                <tr>
                        <td>
                           <h5> ${item.title} </h5>
                        </td>
                </tr>
                <tr>
                        <td>
                            <img src="${item.images[0]}" style="width:100px;height:100px"></img>
                        </td>
                </tr>

               
                <tr>
                    <div>
                        <td>
                            <span style="font-weight:bold;" class="label label-warning">
                            ${item.price}
                            </span>
                        </td>
                        <td>
                            <button class="btn btn-default">
                                <a href="${item.url}">BUY</a>
                            </button>
                        </td>
                    </div>
                </tr>
                <tr>

                </tr>
            </table>
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
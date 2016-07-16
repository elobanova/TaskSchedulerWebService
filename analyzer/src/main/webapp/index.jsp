<html>
<head>
    <link rel="stylesheet" type="text/css" href="./resources/styles/datatables.min.css">
    <link rel="stylesheet" type="text/css" href="./resources/styles/app.css">
    <script src="./resources/js/jquery-3.1.0.js"></script>
    <script src="./resources/js/jquery.dataTables.min.js"></script>
    <script src="./resources/js/app.js"></script>

</head>

<body>
<table id="results" class="table table-striped table-bordered dataTable no-footer" cellspacing="0" width="50%"
       style="max-width: 700px">
    <thead>
    <tr>
        <th></th>
        <th>Url</th>
        <th>Status</th>
    </tr>
    </thead>
</table>


<form class="form-group" id="main" action="#" onsubmit="return false" style="width: 50%; max-width:700px">
    <label for="url"> URL of the Web Page to analyze: </label>
    <input type="text" class="form-control" id="url" name="url"/>
    <input id="process" class="btn btn-primary" type="submit" value="Process"/>
    <div class="error" id="error-container" style="display: none">
        <ul class="list-unstyled">
            <li id="error"></li>
        </ul>
    </div>
</form>


</body>
</html>

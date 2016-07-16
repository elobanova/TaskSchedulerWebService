<html>
<head>
    <link rel="stylesheet" type="text/css" href="./resources/styles/datatables.min.css">
    <link rel="stylesheet" type="text/css" href="./resources/styles/app.css">
    <script src="./resources/js/jquery-3.1.0.js"></script>
    <script src="./resources/js/jquery.dataTables.min.js"></script>
    <script src="./resources/js/app.js"></script>

</head>

<body>
<table id="results" class="table table-striped table-bordered dataTable no-footer" cellspacing="0" width="50%" style="max-width: 700px">
    <thead>
    <tr>
        <th></th>
        <th>Url</th>
        <th>Status</th>
    </tr>
    </thead>
</table>


<form id="main" action="#" onsubmit="return false" style="width: 50%; max-width:700px">
    URL of the Web Page to analyze: <input type="text" name="url">
    <input id="process" type="submit" value="Process">
</form>


</body>
</html>

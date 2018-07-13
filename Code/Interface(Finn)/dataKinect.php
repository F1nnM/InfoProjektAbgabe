<?php
header('Content-Type: text/event-stream');
header('Cache-Control: no-cache');
session_start();

echo "retry: 500\n";

$dbhost = "localhost:3306";
$dbuser = "root";
$dbpass = "";
$conn = mysqli_connect($dbhost, $dbuser, $dbpass);
if(! $conn ) {
    die('Could not connect: ' . mysql_error());
}
mysqli_select_db($conn, "data");
$data = "data: [";
$result = mysqli_query($conn, "SELECT `ID`, `JsonString` FROM `current3D` WHERE `FETCHED`!=TRUE;");
if($result==false){
    die(mysqli_error($conn));
}
if (mysqli_num_rows($result) > 0) {
    $counter = 0;
    foreach ($result as $row) {
        $data = $data.$row["JsonString"];
        //mysqli_query($conn, "UPDATE `current3D` SET `FETCHED` = '1' WHERE `current3D`.`ID` = $row[ID];");
        $counter++;
    }
}

$data = rtrim($data, ",");
echo $data."]\n\n";
flush();
?>

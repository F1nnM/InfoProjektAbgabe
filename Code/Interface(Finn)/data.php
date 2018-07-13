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
$data = "data: {";
$result = mysqli_query($conn, "SELECT `X`, `Y`, `ID` FROM `current` WHERE `FETCHED`!=TRUE AND TYPE=\"Sensor\";");

if (mysqli_num_rows($result) > 0) {
    $result = mysqli_fetch_all($result, MYSQLI_ASSOC);
    $counter = 0;
    foreach ($result as $row) {
        $data = $data."\"Sens$counter\": {\"x\": $row[X], \"y\": $row[Y]},";
       // mysqli_query($conn, "UPDATE `current` SET `FETCHED` = '1' WHERE `current`.`ID` = $row[ID];");
        $counter++;
    }
}

$r = mysqli_query($conn, "SELECT `X`, `Y` FROM `current` WHERE TYPE=\"Position\" ORDER BY TIMESTAMP DESC LIMIT 1;");
if (mysqli_num_rows($r) > 0) {
    $r = mysqli_fetch_all($r, MYSQLI_ASSOC);
    $data = $data."\"Pos\": {\"x\": ".$r[0]["X"].", \"y\": ".$r[0]["Y"]."}";
}

$data = rtrim($data, ",");
echo $data."}\n\n";
flush();
?>

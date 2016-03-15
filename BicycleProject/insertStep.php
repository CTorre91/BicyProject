<?php
$servername = "bicyproject.esy.es";
$username = "u916366449_Admin";
$password = "tQeDPf7KAG";
$dbname = "u916366449_Bicy";

//$id = $_POST['id'];
$latitude = $_POST['latitude'];
$longitude =  $_POST['longitude'];
$altitude =  $_POST['altitude'];
$phoneid =  $_POST['phoneid'];
$time =  $_POST['time'];

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
	die("Connection failed: " . $conn->connect_error);
}

$sql = "INSERT INTO Pasos (Latitud, Longitud, Altura, IDphone, Tiempo)
VALUES ($latitude, $longitude, $altitude, $phoneid, $time)";

if ($conn->query($sql) === TRUE) {
	echo "New record created successfully";
} else {
	echo "Error: " . $sql . "<br>" . $conn->error;
}


$conn->close();
?>
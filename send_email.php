<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $nume = $_POST["nume"];
    $email = $_POST["email"];
    $subiect = $_POST["subiect"];
    $mesaj = $_POST["mesaj"];
    
    // Validare email
    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        echo "Email invalid!";
        exit;
    }

    // Setare destinatar
    $to = "travelgenius.contact@gmail.com"; // Adresa ta de email
    $subject = $subiect ? $subiect : "Mesaj de la " . $nume; // Dacă nu este setat un subiect, se va folosi numele utilizatorului
    $message = "Nume: " . $nume . "\n";
    $message .= "Email: " . $email . "\n\n";
    $message .= "Mesaj:\n" . $mesaj . "\n";
    
    // Setare header
    $headers = "From: " . $email;

    // Trimiterea emailului
    if (mail($to, $subject, $message, $headers)) {
    header("Location: multumesc.html");
    exit;
} else {
    echo "Eroare la trimiterea mesajului. Vă rugăm să încercați mai târziu.";
}
}
?>

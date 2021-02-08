<html>
<head><title>Abgabe einreichen</title>
<style>
    .textbox{
        width: 100%;
    }
    #textarea{
        width:80%;
        height: 40%;
        font-size: 150%;
    }
    .btn{
        margin-top: 5px;
        width:10%;
        height:5%;
        cursor: hand;
        font-size: 120%;
    }

</style>

<body>
    <h1> Kurs: ${course.name} </h1>
    <h1> Aufgabe: ${assignment.name}</h1>
    <h1>Beschreibung: ${assignment.description}</h1>
  <form name="submission" action="new_assignment" method="post" id="submissionText">
    <div class = "textbox"><h1>Abgabetext: </h1><textarea name="text" id="textarea" form="submissionText"/></textarea></div>
    <input type="hidden" name="kid" value=${course.kid}><input type="hidden" name="aid" value=${assignment.aid}>
    <input type="submit" class="btn" value="Einreichen"/>
  </form>


</body>
</html>
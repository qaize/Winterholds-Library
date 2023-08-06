"use strict";

function doBan() {
  let text;
  if (confirm("Are You Sure want to ban this user ?") == true) {
    text = "You pressed OK!";
  } else {
    text = "You canceled!";
  }
  document.getElementById("demo").innerHTML = text;
}
const prevButton = document.getElementById("prev-button");
const nextButton = document.getElementById("next-button");
const listAuthor = document.getElementById("table-author");
const searchAuthor = document.getElementById("search-author");
const searchButton = document.getElementById("search-button-author");

let totalPage = 0;

var page = 1;
var nama = "";

searchAuthor.addEventListener("keyup", () => {
  nama = searchAuthor.value;
  loadAuthorData();
});

prevButton.addEventListener("click", () => {
  if (page > 1) {
    page--;
    loadAuthorData();
  }
});

nextButton.addEventListener("click", () => {
  if (totalPage > page) {
    page++;
    loadAuthorData();
  }
});

function loadAuthorData() {
  let url = `http://localhost:7081/winterhold/api/author/getAuthorWithPage?page=${page}&nama=${nama}`;
  $.ajax({
    url: url,
    type: "GET",
    success: function (user) {
      totalPage = user.metaData.totalPage;
      console.log(user);
      if (user.metaData.totalPage >= page) {
        listAuthor.innerHTML = "";
        $.each(user.data, function (key, value) {
          $("#table-author").append(`
          <tr>
            <td>
                <div class="button-wrapper">
                    <a class="btn blue-button" href="/winterhold/author/detail?id=${value.id}">Books</a>
                    <a class="btn update-button" href="/winterhold/author/update?id=${value.id}"">Edit</a>
                    <a class="btn warning-button" href="/winterhold/author/delete?id=${value.id}"">Delete</a>
                </div>
            </td>
            <td>${value.fullname}</td>
            <td>${value.age}</td>
            <td>${value.deceasedDateStr}</td>
            <td>${value.education}</td>
            <td>${value.createdBy}</td>
            <td>${value.modifiedBy}</td>
          </tr>

          `);
        });
      }
    },
  });
}

loadAuthorData();

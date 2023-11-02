const prevButton = document.getElementById("prev-button");
const nextButton = document.getElementById("next-button");
const listAuthor = document.getElementById("table-author");
const searchAuthor = document.getElementById("search-author");
const searchButton = document.getElementById("search-button");
const paginationList = document.getElementById("pagination-list");
const paginationCurrent = document.getElementById("pagination-current");
const paging = document.getElementById("paging");

let totalPage = 0;

var page = 1;
var nama = "";

// let role = document.querySelector(".role");
// if (role != null) {
//   let roleName = role.textContent;
//   let formatted = roleName
//     .replace("[", "")
//     .replace("]", "")
//     .replace("ROLE_", "");
//   role.textContent = formatted;
// } else {
//   let mainBody = document.querySelector(".main-body");
//   let roleName = mainBody.getAttribute("data-role");
//   let formatted = roleName
//     .replace("[", "")
//     .replace("]", "")
//     .replace("ROLE_", "");
//   mainBody.setAttribute("data-role", formatted);
// }

searchAuthor.addEventListener("keyup", () => {
  nama = searchAuthor.value;
  loadAuthorData();
});

searchButton.addEventListener("click", () => {
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
      paging.style.display = "flex";
      paginationList.innerHTML = "";
      paginationCurrent.innerHTML = `page ${page} of ${totalPage}`;

      // if true pagination not showing
      if (totalPage <= 1) {
        paging.style.display = "none";
      }

      // Generating Button lists
      for (let i = 0; i < totalPage; i++) {
        const button = document.createElement("button");
        button.innerHTML = 1 + i;

        button.addEventListener("click", () => {
          page = i + 1;
          loadAuthorData();
        });

        paginationList.appendChild(button);
      }

      // insert data to table with Next and prev paging
      if (user.metaData.totalPage >= page) {
        listAuthor.innerHTML = "";
        $.each(user.data, function (key, value) {
          let adminAction = ``;

          if (role.textContent === "administrator") {
            adminAction = `
            <a class="btn update-button" href="/winterhold/author/update?id=${value.id}">Edit</a>
            <a class="btn warning-button" href="/winterhold/author/delete?id=${value.id}">Delete</a>`;
          }

          $("#table-author").append(
            `
            <tr>
              <td>
                  <div class="button-wrapper">
                      <a class="btn blue-button" href="/winterhold/author/detail?id=${value.id}">Books</a>` +
              adminAction +
              `
                  </div>
              </td>
              <td>${value.fullname}</td>
              <td>${value.age}</td>
              <td>${value.deceasedDateStr}</td>
              <td>${value.education}</td>
              <td>${value.createdBy}</td>
              <td>${value.modifiedBy}</td>
            </tr>

            `
          );
        });
      }
    },
  });
}

loadAuthorData();

  <footer class="py-3 my-4">
    <hr />
    <!-- ul class="nav justify-content-center border-bottom pb-3 mb-3">
      <li class="nav-item"><a href="#" class="nav-link px-2 text-muted">Home</a></li>
      <li class="nav-item"><a href="#" class="nav-link px-2 text-muted">Features</a></li>
      <li class="nav-item"><a href="#" class="nav-link px-2 text-muted">Pricing</a></li>
      <li class="nav-item"><a href="#" class="nav-link px-2 text-muted">FAQs</a></li>
      <li class="nav-item"><a href="#" class="nav-link px-2 text-muted">About</a></li>
    </ul -->
    <p class="text-center text-muted">&copy; 2024 Confluent</p>
    <img class="d-block mx-auto mb-4" src="../assets/blue-confluent.svg" alt="" width="50" height="50">
  </footer>
 <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
 <script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/6.65.7/codemirror.min.js" integrity="sha512-8RnEqURPUc5aqFEN04aQEiPlSAdE0jlFS/9iGgUyNtwFnSKCXhmB6ZTNl7LnDtDWKabJIASzXrzD0K+LYexU9g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
 <script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/6.65.7/mode/properties/properties.min.js" integrity="sha512-P4OaO+QWj1wPRsdkEHlrgkx+a7qp6nUC8rI6dS/0/HPjHtlEmYfiambxowYa/UfqTxyNUnwTyPt5U6l1GO76yw==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
 <script src="https://cdnjs.cloudflare.com/ajax/libs/dropzone/5.9.3/dropzone.min.js" integrity="sha512-U2WE1ktpMTuRBPoCFDzomoIorbOyUv0sP8B+INA3EzNAhehbzED1rOJg6bCqPf/Tuposxb5ja/MAUnC8THSbLQ==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>

 <script>
    var editor2 = CodeMirror.fromTextArea(log, {
      lineNumbers: true
      //mode: "properties"
      //mode: {name: "javascript", json: true}
    });
    editor2.setSize("100%", 650);
 </script>
 <!-- codemirror for properties files -->
  <script>
    var editor = CodeMirror.fromTextArea(textarea, {
      lineNumbers: true,
      mode: "properties"
      //mode: {name: "javascript", json: true}
    });
    editor.setSize("100%", "100%");
  </script>
  <!-- DROPZONE -->
  <script>
          $(function () {
              Dropzone.options.dropzone = {
                  maxFilesize: 9096, // MB
                  parallelUploads: 16
              };
              $("dropzone").dropzone({ url: "/upload", maxFilesize: 9 });
          });
  </script>
  <script>
    /* Popper stuff https://popper.js.org/docs/v2/tutorial/
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
      return new bootstrap.Popover(popoverTriggerEl)
    })  */

    // Modal overlay with Spinner
    //var modal = new bootstrap.Modal(document.getElementById("loading"), {});
    const myModal = new bootstrap.Modal(document.getElementById('loading'), {}); // options
    myModal.hide();

    const modalTrigger = document.querySelectorAll('.dropdown-item');
    modalTrigger.forEach(el => el.addEventListener('click', event => {
      myModal.show();
    }));

   // Did not work
   // var modalTrigger = [].slice.call(document.querySelectorAll('.dropdown-item'));
   // modalTrigger.map(function (el) {addEventListener("click", console.log("click!"))});

    // Toast
    var toastElList = [].slice.call(document.querySelectorAll('.toast'))
    var toastList = toastElList.map(function (toastEl) {
      return new bootstrap.Toast(toastEl)
    })
    toastList.forEach(toast => toast.show())
  </script>
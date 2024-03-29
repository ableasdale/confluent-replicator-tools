<div class="container">
  <footer class="py-3 my-4 border-top">
    <ul class="nav justify-content-center border-bottom pb-3 mb-3">
      <li class="nav-item"><a href="https://github.com/ableasdale/confluent-replicator-tools" class="nav-link px-2 text-muted">This Project on GitHub</a></li>
      <li class="nav-item"><a href="https://docs.confluent.io/platform/current/multi-dc-deployments/replicator/index.html" class="nav-link px-2 text-muted">Replicator Documentation</a></li>
      <li class="nav-item"><a href="https://docs.confluent.io/platform/current/multi-dc-deployments/replicator/replicator-quickstart.html#rep-quickstart-monitoring" class="nav-link px-2 text-muted">Configuring Replicator Monitoring in C3</a></li>
      <li class="nav-item"><a href="https://docs.confluent.io/platform/current/multi-dc-deployments/replicator/replicator-tuning.html" class="nav-link px-2 text-muted">Tuning Replicator</a></li>
      <li class="nav-item"><a href="https://docs.confluent.io/platform/current/installation/versions-interoperability.html" class="nav-link px-2 text-muted">Supported Versions</a></li>
    </ul>
     <p class="text-center text-body-secondary">&copy; 2024 Confluent</p>
     <a href="http://confluent.io"><img class="d-block mx-auto" src="../assets/blue-confluent.svg" alt="" width="40" height="40"></a>
  </footer>
</div>
<!- Javascript below -->
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
    let dropzone = new Dropzone("#dropzone", { url: "/upload", maxFilesize: 9096,maxFilesize: 9096, parallelUploads: 1});
    dropzone.on("success", file => { location.reload(); });
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
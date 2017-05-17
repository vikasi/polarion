<?xml version="1.0" encoding="UTF-8"?>
<script type="text/javascript">
    var anchor = document.location.hash;
    if ((anchor != null) && (anchor.length == 0)) {
        anchor = null;
    }
    var redir = '<%=request.getAttribute("redir")%>';
    if (anchor != null) {
        redir += "&anchor=" + encodeURIComponent(anchor.substring(1, anchor.length));
    }
    document.location.href = redir;
</script>

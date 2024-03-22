const getFile = async (url, searchPayload) => {
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;

    const response = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        },
        body: JSON.stringify(searchPayload)
    })
    if (!response.ok) throw new Error("Something was wrong on backend!")

    const fileName = getFileNameFromHeader(response);

    response.blob().then(blob => {
        // Create a blob URL and create a link element to trigger the download
        const blobUrl = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.style.display = 'none';
        a.href = blobUrl;
        a.download = fileName; // Set the desired file name with extension
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(blobUrl);
    })

}

const getFileNameFromHeader = (response) => {
    const regExpFilename = /filename="(?<filename>.*)"/;
    return regExpFilename.exec(response.headers.get("content-disposition"))
        ?.groups
        ?.filename ?? null
}
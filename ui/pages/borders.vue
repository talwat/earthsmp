<script setup lang="ts">
    let preview = ref(`/api/borders?${Date.now()}`)

    async function change($event: Event) {
        const target = $event.target as HTMLInputElement;
        if (target && target.files) {
            await $fetch("/api/upload/borders", {
                "body": await target.files[0].bytes(),
                "method": "POST",
                "headers": { "Content-Type": "application/octet-stream" }
            })
        }
        preview.value = `/api/borders?${Date.now()}`
    }
</script>

<template>
    <h1>Upload Borders</h1>
    <div class="upload-preview-container">
        <img class="preview-image" :src="preview">
        <input class="upload" id="upload" type="file" @change="change($event)" accept="image/png" capture />
        <label class="upload-label" for="upload">Upload File</label>
    </div>
</template>

<style lang="css" scoped>
    .preview-image {
        width: 50%;
    }

    .upload {
        display: none;
    }

    .upload-label {
        cursor: pointer
    }

    .upload-preview-container {
        display: flex;
        flex-direction: column;
        gap: 1em;
    }
</style>
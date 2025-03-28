<script setup lang="ts">
const preview = ref(`/api/borders.png?t=${Date.now()}`);

async function change($event: Event) {
  const target = $event.target as HTMLInputElement;
  if (target && target.files) {
    await $fetch("/api/upload/borders.png", {
      body: await target.files[0].bytes(),
      method: "POST",
      headers: { "Content-Type": "application/octet-stream" },
    });
  }
  preview.value = `/api/borders.png?t=${Date.now()}`;
}
</script>

<template>
  <h1>Upload Borders</h1>
  <div class="upload-preview-container">
    <img class="preview-image" :src="preview" />
    <input
      id="upload"
      class="upload"
      type="file"
      accept="image/png"
      capture
      @change="change($event)"
    />
    <label class="upload-label" for="upload">Upload File</label>
  </div>
  <a download="borders.png" :href="preview">Download File</a>
</template>

<style lang="css" scoped>
.preview-image {
  width: 50%;
}

.upload {
  display: none;
}

.upload-label {
  cursor: pointer;
}

.upload-preview-container {
  display: flex;
  flex-direction: column;
  gap: 1em;
}
</style>

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
  <h1>Borders</h1>
  <div class="container">
    <img class="preview-image" :src="preview" />
    <div class="options">
      <input
        id="upload"
        class="upload"
        type="file"
        accept="image/png"
        capture
        @change="change($event)"
      />
      <label class="btn upload-label" for="upload">Upload File</label>
      <span class="info">8-bit RGBA</span>
      <a class="btn" download="borders.png" :href="preview">Download File</a>
    </div>
  </div>
</template>

<style lang="css" scoped>
.container {
  display: flex;
  flex-direction: column;
  gap: 1em;
  align-items: center;
}

.preview-image {
  object-fit: contain;
  max-width: 100%;
  max-height: calc(100vh - 16em);
  border: 1px solid black;
}

.info {
  font-style: italic;
}

.upload {
  display: none;
}

.upload-label {
  cursor: pointer;
}

.options {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  gap: 1em;
  width: 100%;
}
</style>

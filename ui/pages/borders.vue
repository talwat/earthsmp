<script setup lang="ts">
import Viewer from "viewerjs";
import "viewerjs/dist/viewer.css";

const preview = ref(`/api/borders.png?t=${Date.now()}`);
let viewer: Viewer;

async function change($event: Event) {
  const target = $event.target as HTMLInputElement;
  if (target && target.files) {
    await $fetch("/api/upload/borders.png", {
      body: await target.files[0].arrayBuffer(),
      method: "POST",
      headers: { "Content-Type": "application/octet-stream" },
    });
  }
  preview.value = `/api/borders.png?t=${Date.now()}`;
  nextTick(() => viewer.update());
}

onMounted(() => {
  viewer = new Viewer(document.getElementById("image")!, {
    inline: true,
    navbar: false,
    tooltip: false,
    title: false,
    toolbar: false,
    zoomRatio: 0.2,
    initialCoverage: 1,
    container: document.getElementById("image-container")!,
  });

  viewer.show();
});

// Refresh every 30 seconds.
setInterval(async () => {
  viewer.update();
}, 30000);
</script>

<template>
  <h1>Borders</h1>
  <div class="container">
    <div id="image-container" class="image-container">
      <img id="image" class="preview-image" :src="preview" />
    </div>
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

<style>
img {
  image-rendering: pixelated;
}
</style>

<style lang="css" scoped>
.container {
  display: flex;
  flex-direction: column;
  gap: 1em;
  align-items: center;
}

.image-container {
  width: 100%;
  height: calc(100vh - 16em);
  max-width: 64em;
  overflow: hidden;
}

.preview-image {
  height: 100%;
  display: none;
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
  align-items: center;
  gap: 1em;
  width: 100%;
}

@media (max-width: 600px) {
  .options {
    justify-content: space-between;
  }
}
</style>

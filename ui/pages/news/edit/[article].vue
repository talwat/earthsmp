<script setup lang="ts">
const route = useRoute();

interface Article {
  body: string;
  headline: string;
  date: string;
}

let date = route.params.article as string;

// @ts-ignore
let article: Ref<Article, Article> = ref(
  await $fetch(`/api/news/${date}?t=${Date.now()}`),
);
let changed = ref(false);

async function upload() {
  await $fetch(`/api/news/edit/${article.value.date}`, {
    method: "PATCH",
    body: article.value,
  });

  // @ts-ignore-error
  article.value = await $fetch(`/api/news/${date}?t=${Date.now()}`);
  changed.value = false;
  alert("Changes saved!");
}
</script>

<template>
  <h1>Article for {{ date }}</h1>
  <div class="editor" @input="changed = true">
    <div class="save-container" v-if="changed">
      <button @click="upload()">Save Changes</button>
    </div>
    <label for="headline">Headline: </label>
    <input id="headline" v-model="article.headline" />
    <br />
    <label for="body">Body: </label>
    <textarea id="body" v-model="article.body" />
  </div>
</template>

<style>
main {
  height: 100%;
  display: flex;
  min-height: calc(100vh - 7em);
  flex-direction: column;
}
</style>

<style lang="css" scoped>
pre {
  display: inline;
}

.save-container {
  display: flex;
  align-items: center;
  flex-direction: column;
}

h1 {
  flex: 0 1 auto;
}

.editor {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  max-width: 40em;
}

label {
  display: block;
  margin-top: 4px;
  margin-bottom: 4px;
}

input,
textarea {
  font-family: monospace;
  box-sizing: border-box;
  max-width: 100%;
}

textarea {
  resize: none;
  min-height: 100%;
  flex-grow: 1;
}
</style>

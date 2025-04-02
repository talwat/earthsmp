<script setup lang="ts">
const route = useRoute();

interface Article {
  body: string;
  headline: string;
  date: string;
}

let date = route.params.article as string;
let article: Article = await $fetch(`/api/news/${date}`);
</script>

<template>
  <h1>{{ new Date(date).toDateString() }}</h1>
  <div class="editor">
    <label for="headline">Headline: </label>
    <input id="headline" v-model="article.headline" />
    <br />
    <label for="body">Body: </label>
    <textarea id="body" v-model="article.body" />
  </div>
</template>

<style lang="css" scoped>
pre {
  display: inline;
}

h1 {
  flex: 0 1 auto;
}

.editor {
  display: flex;
  flex-direction: column;
  flex: 1 1 auto;
  height: 100%;
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
  height: 32em;
}
</style>

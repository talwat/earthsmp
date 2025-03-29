<script setup lang="ts">
import type { UUID } from "crypto";
import yaml from "yaml";

interface Territory {
  tag: string;
  name: string;
  color: number;
  colony: boolean;
}

interface Nation {
  tag: string;
  name: string;
  nick: string;
  color: number;
  flag: string;
  ruler: UUID;
  members: UUID[];
  territories: Territory[];
}

interface Nations {
  nations: Nation[];
}

const url = ref(`/api/nations.yml?t=${Date.now()}&encoding=utf8`);
const nations: Nations = await content();

async function content() {
  const text = (await $fetch(url.value)) as string;
  const parsed = yaml.parse(text);

  return parsed;
}

function formatColor(color: number) {
  return `hsl(${color}, 100%, 50%)`;
}
</script>

<template>
  <h1>Nations</h1>
  <ul>
    <li v-for="nation in nations.nations">
      <h2>{{ nation.nick }}</h2>
      <h3>({{ nation.name }})</h3>
      <div class="color-container">
        <div
          class="box"
          :style="{ backgroundColor: formatColor(nation.color) }"
        ></div>
        <span>Color</span>
      </div>
    </li>
  </ul>
</template>

<style lang="css" scoped>
.box {
  height: 20px;
  width: 20px;
}

.color-container {
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  gap: 0.5em;
}
</style>

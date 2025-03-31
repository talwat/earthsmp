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
const raw: Nations = await content();
raw.nations.sort((a, b) => (b.members?.length || 0) - (a.members?.length || 0));
const nations = ref(raw);

const knownPlayers = await $fetch("/api/usercache");

async function content() {
  const text = (await $fetch(url.value)) as string;
  const parsed = yaml.parse(text);

  return parsed;
}

function formatColor(color: number) {
  return `hsl(${color}, 100%, 50%)`;
}

function addMember(nation: Nation) {
  nation.members.push("00000000-0000-0000-0000-000000000000");
}

function deleteMember(nation: Nation, i: number) {
  if (nation.members[i].length == 0) {
    nation.members.splice(i, 1);
  }
}
</script>

<template>
  <h1>Nations</h1>
  <div class="nations-container">
    <div class="nation" v-for="nation in nations.nations">
      <h2>
        <pre>{{ nation.tag }}</pre>
        - {{ nation.nick }}
      </h2>
      <table>
        <tbody>
          <tr>
            <th>Tag</th>
            <td>
              <input class="code" v-model="nation.tag" />
            </td>
          </tr>
          <tr>
            <th>Common Name</th>
            <td><input v-model="nation.nick" /></td>
          </tr>
          <tr>
            <th>Full Name</th>
            <td><input v-model="nation.name" /></td>
          </tr>
          <tr>
            <th>Color</th>
            <td class="color-container">
              <div
                class="box"
                :style="{ backgroundColor: formatColor(nation.color) }"
              ></div>
              <input v-model="nation.color" />
            </td>
          </tr>
          <tr>
            <th>Flag</th>
            <td>
              <input class="code" v-model="nation.flag" placeholder="No Flag" />
            </td>
          </tr>
          <tr>
            <th>Members</th>
            <td>
              <ul>
                <li v-for="(member, i) in nation.members">
                  <div class="player-container">
                    <button @click="nation.members.splice(i, 1)">X</button>
                    <input
                      @blur="deleteMember(nation, i)"
                      class="code uuid"
                      v-model="nation.members[i]"
                      :class="{ 'member-ruler': nation.ruler == member }"
                    />
                    <span v-if="knownPlayers[member]" class="player-name">{{
                      knownPlayers[member]
                    }}</span>
                    <span v-else class="disabled player-name">Unknown</span>
                  </div>
                </li>
                <li><button @click="addMember(nation)">+</button></li>
              </ul>
            </td>
          </tr>
          <tr>
            <th>Ruler</th>
            <td class="player-container">
              <input class="code uuid" v-model="nation.ruler" />
              <span v-if="knownPlayers[nation.ruler]" class="player-name">{{
                knownPlayers[nation.ruler]
              }}</span>
              <span v-else class="disabled player-name">Unknown</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style lang="css" scoped>
.nations-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1em;
}

.nation {
  width: 100%;
}

.member-ruler {
  text-decoration: underline;
}

input {
  width: 100%;
  box-sizing: border-box;
  border: none;
  padding: 0;
  margin: 0;
}

.code {
  font-family: monospace;
}

.disabled {
  color: gray;
}

pre {
  overflow: scroll;
  margin: 0;
  white-space: pre-wrap;
  display: inline;
}

table {
  border: 1px solid;
  border-collapse: collapse;
  width: 100%;
}

tr {
  border-bottom: 1px solid black;
}

th {
  padding: 0.3em;
  border-right: 1px solid black;
  text-align: left;
  width: 8em;
}

td {
  padding: 0.3em;
}

.player-name {
  font-style: italic;
}

.player-container {
  gap: 1em;
  min-height: 1.5em;
  display: flex;
}

.uuid {
  width: 36ch;
}

.color-container {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 0.5em;
}

.box {
  height: 20px;
  min-width: 20px;
}
</style>

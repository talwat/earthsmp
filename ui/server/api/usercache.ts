import { readFile } from "fs/promises";

interface Player {
  name: String;
  uuid: String;
}

export default defineEventHandler(async (event) => {
  try {
    let players: Player[] = JSON.parse(
      await readFile(`${process.env.SERVER_PATH}/usercache.json`, "utf8"),
    );
    let result = new Map();

    for (const player of players) {
      result.set(player.uuid, player.name);
    }

    return Object.fromEntries(result);
  } catch {
    throw createError({
      statusCode: 404,
      statusMessage: "Usercache doesn't exist yet",
    });
  }
});

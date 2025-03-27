import { readFile, writeFile, mkdir } from "fs/promises"
import { DATA_PATH } from "~/server/paths";

export default defineEventHandler(async (event) => {
  try {
    return readFile(`${DATA_PATH}/borders.png`)
  } catch(e) {
    throw createError({
      statusCode: 404,
      statusMessage: 'Borders image does not exist yet'
    })  
  }
});

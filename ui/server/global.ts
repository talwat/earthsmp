export const DATA_PATH = `${process.env.SERVER_PATH}/plugins/EarthSMP`;
export const ALLOWED_FILES = ["borders.png", "nations.yml"];
export const PAGES = [
  ["/api/auth/signout", "Sign Out"],
  ["/news", "News"],
  ["/borders", "Borders"],
  ["/nations", "Nations"],
];
export const PAGES_JOURNALIST = [
  ["/api/auth/signout", "Sign Out"],
  ["/news", "News"],
];

export const NEWS_PATH = `${DATA_PATH}/news`;

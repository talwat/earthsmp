interface User {
  role: string;
}

export default defineNuxtRouteMiddleware(async (to) => {
  if (
    to.path.startsWith("/api") ||
    to.path.startsWith("/auth") ||
    to.path == "/"
  ) {
    return;
  }

  const { loggedIn, user } = useUserSession();
  const data = user.value as User | null;

  if (!loggedIn.value) {
    return navigateTo("/");
  }

  let role = data?.role;

  if (role) {
    if (role == "admin") {
      return;
    }

    if (role == "journalist") {
      if (to.path == "/" || to.path.startsWith("/news")) {
        return;
      } else {
        return navigateTo("/");
      }
    }
  } else {
    return navigateTo("/");
  }
});

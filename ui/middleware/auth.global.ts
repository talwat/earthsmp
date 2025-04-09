interface User {
  role: string;
}

export default defineNuxtRouteMiddleware(async (to) => {
  if (to.path.startsWith("/api")) {
    return;
  }

  const data = useAuth();
  let session = await data.getSession({
    required: true,
  });

  if (data.status.value !== "authenticated" || !session) {
    data.signIn(undefined, { callbackUrl: to.path });
  }

  let user = data.data.value?.user as User | undefined;
  let role = user?.role;

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
    data.signIn(undefined, { callbackUrl: to.path });
  }
});

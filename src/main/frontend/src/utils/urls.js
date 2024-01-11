export const getBackUrl = () => {
  const host = window.location.host;
  if (host.includes("localhost")) {
    return "http://localhost:8080";
  } else {
    return "https://back.mpftree.site";
  }
};

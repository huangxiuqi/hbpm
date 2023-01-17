import {defineConfig} from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig(env => {
  return {
    plugins: [react()],
    base: '/login',
    build: {
      outDir: 'build',
      assetsDir: 'static',
      sourcemap: true,
    },
    server: {
      port: 3000,
      origin: 'http://localhost:3000',
    },
  }
});

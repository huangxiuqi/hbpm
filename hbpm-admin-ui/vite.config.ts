import {defineConfig} from 'vite';
import react from '@vitejs/plugin-react';
import { visualizer } from "rollup-plugin-visualizer";

// https://vitejs.dev/config/
export default defineConfig(env => {
  return {
    plugins: [
      react(),
      visualizer(),
    ],
    build: {
      outDir: 'build',
      assetsDir: 'static',
      sourcemap: true,
      chunkSizeWarningLimit: 1600,
    },
    resolve: {
      alias: {
        '@': '/src',
      },
    },
    server: {
      port: 3001,
      origin: 'http://localhost:3001',
    },
  }
});

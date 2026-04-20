<template>
  <section class="home-page fade-in">
    <div class="hero-wrap">
      <h1 class="hero-title">云澜双智・菜狗 AI 服务平台</h1>
      <p class="hero-subtitle">/ 萌宠暖心相伴，智脑全能护航 /</p>

      <div class="cards">
        <RouterLink to="/pet" class="app-card pet-card">
          <div class="card-image pet-image" aria-hidden="true">🐱</div>
          <h2>AI宠物咨询</h2>
          <p>智能宠物顾问，帮你解答爱宠烦恼</p>
          <span class="cta-btn">立即体验 →</span>
        </RouterLink>

        <RouterLink to="/agent" class="app-card agent-card">
          <div class="card-image agent-image" aria-hidden="true">🤖</div>
          <h2>AI超级智能体</h2>
          <p>全能型AI助手，解决各类专业问题</p>
          <span class="cta-btn">立即体验 →</span>
        </RouterLink>
      </div>
    </div>

    <footer class="home-footer">
      <div class="footer-col">
        <h3>菜狗AI超级智能体应用平台</h3>
        <a href="javascript:void(0)">《用户协议》</a>
        <a href="javascript:void(0)">《隐私政策》</a>
      </div>
      <div class="footer-col">
        <h3>友情链接</h3>
        <a href="javascript:void(0)">宠物之家</a>
        <a href="javascript:void(0)">玩转AI</a>
      </div>
      <div class="footer-col">
        <h3>联系我们</h3>
        <a href="javascript:void(0)">商务合作</a>
        <span>站长：菜狗</span>
      </div>
    </footer>
  </section>
</template>

<script setup>
import { RouterLink } from 'vue-router'
</script>

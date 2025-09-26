# 🚀 GitHub Actions CI/CD Pipeline - Deployment Setup

## ✅ **Files Created Successfully!**

Your GitHub Actions CI/CD pipeline has been implemented with the following files:

### **📁 Pipeline Files:**
- `.github/workflows/ci-cd.yml` - Main CI/CD workflow
- `online-voting-backend-1/Dockerfile.prod` - Production backend container
- `onlinevotingsystem/Dockerfile.prod` - Production frontend container
- `docker-compose.prod.yml` - Production deployment configuration
- `.env.prod` - Production environment template

---

## 🔧 **Next Steps to Complete Setup:**

### **Step 1: Configure GitHub Repository Settings**

#### **A. Enable GitHub Container Registry**
1. Go to your GitHub repository: `https://github.com/siddartha30035/onlinevoting-fin-`
2. Settings → Actions → General
3. Set "Workflow permissions" to "Read and write permissions"
4. Check "Allow GitHub Actions to create and approve pull requests"

#### **B. Add Repository Secrets**
Go to Settings → Secrets and variables → Actions, click "New repository secret" and add:

```bash
# Production Server Details
PROD_HOST=your_production_server_ip
PROD_USER=your_ssh_username  
PROD_SSH_KEY=your_private_ssh_key_content

# Database Credentials
DB_ROOT_PASSWORD=SecureRootPassword123!
DB_USER=voting_user
DB_PASSWORD=SecureUserPassword456!

# Application Security
JWT_SECRET=your_jwt_secret_key_at_least_256_bits_long_for_security
ENCRYPTION_KEY=your_32_character_encryption_key

# Monitoring
GRAFANA_PASSWORD=SecureGrafanaPassword789!
```

#### **C. Create Environment Protection**
1. Settings → Environments
2. Click "New environment" → Name: `production`
3. Add protection rules:
   - ✅ Required reviewers (add yourself)
   - ✅ Wait timer: 0 minutes
   - ✅ Deployment branches: Selected branches → `main`

---

### **Step 2: Production Server Setup**

#### **A. Server Requirements**
- Ubuntu 20.04+ or similar Linux distribution
- Minimum 2GB RAM, 2 CPU cores
- 20GB+ disk space
- Docker and Docker Compose installed

#### **B. Install Docker on Production Server**
```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker $USER

# Install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Verify installation
docker --version
docker-compose --version
```

#### **C. Setup Application Directory**
```bash
# Create application directory
sudo mkdir -p /opt/voting-app
sudo chown $USER:$USER /opt/voting-app
cd /opt/voting-app

# Create monitoring directory
mkdir -p monitoring/grafana/provisioning
mkdir -p mysql
```

#### **D. Copy Configuration Files**
Upload these files to `/opt/voting-app/`:
- `docker-compose.prod.yml`
- `.env` (copy from `.env.prod` and update values)

#### **E. Create Monitoring Configuration**
```bash
# Create prometheus.yml
cat > monitoring/prometheus.yml << 'EOF'
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'voting-backend'
    static_configs:
      - targets: ['backend:9090']
    metrics_path: '/actuator/prometheus'
    scrape_interval: 30s

  - job_name: 'voting-frontend'
    static_configs:
      - targets: ['frontend:80']
    scrape_interval: 30s
EOF
```

---

### **Step 3: SSH Key Setup**

#### **A. Generate SSH Key Pair (if you don't have one)**
```bash
# On your local machine
ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
# Save as: ~/.ssh/voting_app_key
```

#### **B. Add Public Key to Production Server**
```bash
# Copy public key to server
ssh-copy-id -i ~/.ssh/voting_app_key.pub user@your_server_ip

# Or manually add to ~/.ssh/authorized_keys on server
```

#### **C. Add Private Key to GitHub Secrets**
```bash
# Copy private key content
cat ~/.ssh/voting_app_key

# Add this content to GitHub secret: PROD_SSH_KEY
```

---

### **Step 4: Test the Pipeline**

#### **A. Commit and Push Changes**
```bash
# Add all files
git add .

# Commit changes
git commit -m "Add GitHub Actions CI/CD pipeline"

# Push to main branch
git push origin main
```

#### **B. Monitor Pipeline Execution**
1. Go to GitHub repository → Actions tab
2. Watch the "Online Voting System CI/CD" workflow
3. Monitor each job: backend-test, frontend-test, security-scan, build-and-push, deploy, health-check

#### **C. Check Deployment**
After successful pipeline run:
```bash
# Check containers on production server
docker-compose -f docker-compose.prod.yml ps

# Check application health
curl http://your_server_ip:9090/actuator/health
curl http://your_server_ip:3000/
```

---

### **Step 5: Access Your Applications**

After successful deployment:
- **Frontend**: `http://your_server_ip:3000`
- **Backend API**: `http://your_server_ip:9090`
- **API Health**: `http://your_server_ip:9090/actuator/health`
- **Prometheus**: `http://your_server_ip:9091`
- **Grafana**: `http://your_server_ip:3001` (admin/your_grafana_password)

---

## 🔍 **Pipeline Features**

Your CI/CD pipeline includes:

✅ **Automated Testing** - Maven tests for backend, npm lint for frontend  
✅ **Security Scanning** - Trivy vulnerability scanner  
✅ **Docker Images** - Multi-stage optimized builds  
✅ **Container Registry** - GitHub Container Registry (ghcr.io)  
✅ **Production Deployment** - Automated with environment protection  
✅ **Health Checks** - Post-deployment verification  
✅ **Monitoring** - Prometheus + Grafana stack  
✅ **Logging** - Centralized container logs  
✅ **Resource Limits** - Memory and CPU constraints  
✅ **Security** - Non-root containers, secrets management  

---

## 🚨 **Troubleshooting**

### **Common Issues:**

1. **Pipeline fails at build stage**
   - Check Java/Node.js versions in workflow
   - Verify Maven/npm dependencies

2. **Docker push fails**
   - Ensure GitHub token has package write permissions
   - Check repository name matches in workflow

3. **Deployment fails**
   - Verify SSH connection to production server
   - Check server has Docker installed
   - Ensure .env file exists on server

4. **Health checks fail**
   - Wait longer for containers to start
   - Check container logs: `docker-compose logs`
   - Verify database connection

### **Useful Commands:**
```bash
# Check pipeline logs
# Go to GitHub → Actions → Select workflow run

# Check production containers
docker-compose -f docker-compose.prod.yml ps
docker-compose -f docker-compose.prod.yml logs

# Restart services
docker-compose -f docker-compose.prod.yml restart

# Update deployment
docker-compose -f docker-compose.prod.yml pull
docker-compose -f docker-compose.prod.yml up -d
```

---

## 🎉 **Congratulations!**

Your full-stack online voting system now has a professional CI/CD pipeline with:
- Automated testing and security scanning
- Production-ready containerization
- Monitoring and logging
- Zero-downtime deployments

**Push your code to the main branch and watch the magic happen!** 🚀

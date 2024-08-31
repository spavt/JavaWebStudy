new Vue({
    el: "#app",
    data() {
        return {
            msg: [],
            adata: {
                id: "",
                name: "",
                sex: "",
                age: "",
                tel: "",
                scores: "",
                school: ""
            },
            show1: true,
            show: false,
            index1: "",
            totalStudents: 0,
            maleStudents: 0,
            femaleStudents: 0,
            averageScore: 0,
            curPage: 1,
            totalPages: 1,
            isDarkTheme: false  // 初始主题状态为浅色
        };
    },
    created() {
        this.fetchData();
    },
    methods: {
        toggleTheme() {
            this.isDarkTheme = !this.isDarkTheme;
            document.body.className = this.isDarkTheme ? 'dark-mode' : 'light-mode';
        },
        addData() {
            const data = encodeURIComponent(JSON.stringify(this.adata));
            axios
                .get("/hotWebDesign_war_exploded/VueInsert?data=" + data)
                .then(response => {
                    if (response.data.status === "success") {
                        this.resetAdata();
                        this.fetchData(); // 添加数据后刷新数据
                        this.openMessage('操作已完成！', 'success');
                    } else {
                        this.openMessage('操作失败！请重试或联系管理员', 'error');
                    }
                })
                .catch(error => {
                    console.log(error);
                });
        },
        resetAdata() {
            this.adata = {
                id: "",
                name: "",
                sex: "",
                age: "",
                tel: "",
                scores: "",
                school: ""
            };
        },
        openMessage(message, type) {
            this.$message({
                showClose: true,
                message,
                type
            });
        },
        fetchData(page = 1) {
            axios
                .get(`/hotWebDesign_war_exploded/vueTest?curPage=${page}`)
                .then(response => {
                    this.msg = response.data.crud;
                    this.curPage = response.data.curPage;
                    this.totalPages = response.data.totalPages;
                    this.calculateStatistics();
                    this.renderChart();
                })
                .catch(error => {
                    console.log(error);
                });
        },
        deleteData(index) {
            this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                axios
                    .get("/hotWebDesign_war_exploded/vueDel?id=" + this.msg[index].id)
                    .then(response => {
                        if (response.data.status === "success") {
                            this.msg.splice(index, 1);
                            this.calculateStatistics();
                            this.renderChart();
                            this.openMessage('删除成功!', 'success');
                        } else {
                            this.openMessage('删除失败！请重试或联系管理员', 'error');
                        }
                    })
                    .catch(error => {
                        console.log(error);
                    });
            }).catch(() => {
                this.openMessage('已取消删除', 'info');
            });
        },
        update(index) {
            this.show1 = false;
            this.show = true;
            this.adata = {...this.msg[index]};
            this.index1 = index;
        },
        updateOk() {
            const data = encodeURIComponent(JSON.stringify(this.adata));
            axios
                .get("/hotWebDesign_war_exploded/VueUpdate?data=" + data)
                .then(response => {
                    if (response.data.status === "success") {
                        this.openMessage('操作已完成！', 'success');
                        this.resetAdata();
                        this.fetchData();
                    } else {
                        this.openMessage('操作失败！请重试或联系管理员', 'error');
                    }
                })
                .catch(error => {
                    console.log(error);
                });
            this.show1 = true;
            this.show = false;
        },
        calculateStatistics() {
            this.totalStudents = this.msg.length;
            this.maleStudents = this.msg.filter(student => student.sex === "男").length;
            this.femaleStudents = this.msg.filter(student => student.sex === "女").length;
            const totalScores = this.msg.reduce((acc, student) => acc + parseFloat(student.scores), 0);
            this.averageScore = (totalScores / this.totalStudents).toFixed(2);
        },
        renderChart() {
            const ctx = document.getElementById('scoreChart').getContext('2d');
            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: this.msg.map(student => student.name),
                    datasets: [{
                        label: 'Scores',
                        data: this.msg.map(student => student.scores),
                        backgroundColor: 'rgba(75, 192, 192, 0.2)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        },
        changePage(page) {
            if (page >= 1 && page <= this.totalPages) {
                this.fetchData(page);
            }
        }
    },
    computed: {
        pages() {
            return Array.from({ length: this.totalPages }, (_, i) => i + 1);
        }
    }
});

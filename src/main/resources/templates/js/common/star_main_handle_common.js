document.addEventListener('DOMContentLoaded', function() {
    function musicPlay(isPlay) {
        var media = document.querySelector('#audioDom');
        if (isPlay && media.paused) {
            media.play();
        }
        if (!isPlay && !media.paused) {
            media.pause();
        }
    }
    function musicInBrowserHandler() {
        setTimeout(function () {
            musicPlay(true)
        }, 0);
    }
    
    function start() {
        var text = [
            "予 娟宝",
            "有人说，人生在世，其爱有三。曰：",
            "日月与卿，日为朝，月为暮，卿为朝朝暮暮",
            "于我而言：你是深渊中希望的光",
            "是那深林中的鹿、海蓝深处的鲸",
            "You are the apple of my eyes❤️"
        ]
        var str = text.join('<br><br>')
        var str_ = ''
        var i = 0
        var content = document.getElementById('contents')
        var timer = setInterval(() => {
            if (str_.length < str.length) {
                str_ += str[i++]
                content.innerHTML = '<p>' + str_ + '<span class="xx" style="opacity: 1;color: white;">_</span></p>'                        //打印时加光标
            } else {
                clearInterval(timer);
                // 移除光标
                content.innerHTML = '<p>' + str_ + '</p>'
            }
        }, 100)
        console.log('start function called');
    }
    
    document.body.addEventListener('touchstart', musicInBrowserHandler);
    document.body.addEventListener('click', musicInBrowserHandler);
    
    $('#startBtn').click(e => {
        $('#startBtn').hide();
        setTimeout(() => {
            start()
            fireworks();
        }, 4000) // 这里的5000 是5s指烟花延时时间
        setTimeout(() => {
            function audioAutoPlay() {
                var audio = document.getElementById('audioDom');
                audio.play();
                audio.volume(0.8)
            }
            audioAutoPlay();
        }, 2000) // 这里的5000 是5s指音乐延时时间
        console.log(updateConfig({ autoLaunch: true }));
    })
    function fireworks() {
        $('.page_two').removeClass('hide');
    }
});
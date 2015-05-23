(ns white-ink.state)

(def drafts
  {:reviewable "Why—though in the early days of Interlace’s internetted teleputers that operated off largely the same fiber-digital grid as the phone companies, the advent of video telephoning (a.k.a. ‘videophony’) enjoyed an interval of huge consumer popularity—callers thrilled at the idea of phone-interfacing both aurally and facially (the little first-generation phone-video cameras being too crude and narrow-apertured for anything much more than facial close-ups) on first-generation teleputers that at that time were little more than high-tech TV sets, though of course they had that little ‘intelligent-agent’ homuncular icon that would appear at the lower-right of a broadcast/cable program and tell you the time and temperature outside or remind you to take your blood-pressure medication or alert you to a particularly compelling entertainment-option now coming up on channel like 491 or something, or of course now alerting you to an incoming video-phone call and then tap-dancing with a little iconic straw boater and cane just under a menu of possible options for response, and callers did lover their little homuncular icons—but why, within like 16 months or 5 sales quarters, the tumescent demand curve for ‘videophony’ suddenly collapsed like a kicked tent, so that, by the year of the depend adult undergarment, fewer than 10% of all private telephone communications utilized any video-image-fiber data-transfers or coincident products and services, the average U.S. phone-user deciding that s/he actually preferred the retrograde old low-tech bell-era voice-only telephonic interface after all, a preferential about-face that cost a good many precipitant video-telephony-related entrepreneurs their shirts, plus destabilizing two highly respected mutual funds that had ground-floored heavily in video-phone technology, and very nearly wiping out the Maryland State employees’ retirement system’s Freddie-Mac fund, a fund whose administrator’s mistress’s brother had been an almost manically precipitant video-phone-technology entrepreneur… and but so why the abrupt consumer retreat back to good old voice-only telephoning?"
   :editable   "It turned out that there was something terribly stressful about visual telephone interfaces that hadn’t been stressful at all about voice-only interfaces. Videophone consumers seemed suddenly to realize that they’d been subject to an insidious but wholly marvelous delusion about conventional voice-only telephony. They’d never noticed it before, the delusion — it’s like it was so emotionally complex that it could be countenanced only in the context of its loss. Good old traditional audio-only phone conversations allowed you to presume that the person on the other end was paying complete attention to you while also permitting you not to have to pay anything even close to complete attention to her. A traditional aural-only conversation — utilizing a hand- held phone whose earpiece contained only 6 little pinholes but whose mouthpiece (rather significantly, it later seemed) contained (62) or 36 little pinholes — let you enter a kind of highway-hypnotic semi-attentive fugue: while conversing, you could look around the room, doodle, fine-groom, peel tiny bits of dead skin away from your cuticles, compose phone-pad haiku, stir things on the stove; you could even carry on a whole separate additional sign-language-and-exaggerated-facial-expression type of conversation with people right there in the room with you, all while seeming to be right there attending closely to the voice on the phone. And yet — and this was the retrospectively marvelous part — even as you were dividing your attention between the phone call and all sorts of other idle little fuguelike activities, you were somehow never haunted by the suspicion that the person on the other end’s attention might be similarly divided. During a traditional call, e.g., as you let’s say performed a close tactile blemish- scan of your chin, you were in no way oppressed by the thought that your phonemate was perhaps also devoting a good percentage of her attention to a close tactile blemish-scan. It was an illusion and the illusion was aural and aurally supported: the phone-line’s other end’s voice was dense, tightly compressed, and vectored right into your ear, enabling you to imagine that the voice’s owner’s attention was similarly compressed and focused . . . even though your own attention was not, was the thing. This bilateral illusion of unilateral attention was almost infantilely gratifying from an emotional standpoint: you got to believe you were receiving somebody’s complete attention without having to return it. Regarded with the objectivity of hindsight, the illusion appears arational, almost literally fantastic: it would be like being able both to lie and to trust other people at the same time."})

(defn- notes-gen [n draft-text]
  (for [n (range 1 n)
        :let [len (count draft-text)]]
    {:text        (str "note " n)
     :draft-index (rand-int len)}))

(def mock-notes-reviewable (notes-gen 14 (drafts :reviewable)))
(def mock-notes-editable (notes-gen 14 (drafts :editable)))

;; define your app data so that it doesn't get over-written on reload
(defonce app-state (atom {:user   nil
                          :drafts [{:text  (drafts :reviewable)
                                    :notes mock-notes-reviewable}
                                   {:text  (drafts :editable)
                                    :notes mock-notes-editable}]}))























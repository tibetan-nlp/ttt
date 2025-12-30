// javascripts/base.js
document.addEventListener('DOMContentLoaded', function () {
  const langRadios = document.querySelectorAll('input[name="lang"]');
  const dictBoxes  = document.querySelectorAll('input[name^="dict"]');

  // language groups
  const groups = {
    all:      Array.from({length: 16}, (_, i) => i),
    english:  [1, 3, 4, 5, 6, 7],
    tibetan:  [2, 8, 9, 10, 11, 12],
    sanskrit: [13, 14, 15]
  };

  // when a radio is selected -------------------------
  langRadios.forEach(radio => {
    radio.addEventListener('change', () => {
      if (radio.value === "custom") return; // custom mode preserves state

      // clear everything
      dictBoxes.forEach(box => box.checked = false);

      if (radio.value === "all") {
        dictBoxes.forEach(box => box.checked = true);
      } else {
        const list = groups[radio.value] || [];
        list.forEach(i => {
          const box = document.querySelector(`input[name="dict${i}"]`);
          if (box) box.checked = true;
        });
      }
    });
  });

  // if user clicks any checkbox -> switch to "custom" radio
  dictBoxes.forEach(box => {
    box.addEventListener('change', () => {
      const customRadio = document.querySelector('input[name="lang"][value="custom"]');
      if (customRadio) customRadio.checked = true;
    });
  });
});
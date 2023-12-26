import React from 'react';
import { Container } from 'react-bootstrap';
import { useInView } from 'react-intersection-observer';

const Section = ({ children }) => {
  const { ref, inView } = useInView({
    triggerOnce: true,
    threshold: 0.5,
  });

  return (
    <div ref={ref} className={`${inView ? 'fade-in' : 'fade-out'}`}>
      {children}
    </div>
  );
};

export default Section;

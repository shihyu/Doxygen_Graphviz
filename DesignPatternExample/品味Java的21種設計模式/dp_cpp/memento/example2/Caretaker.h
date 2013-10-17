#pragma once

#include "Memento.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace memento
			{
				namespace example2
				{
					///
					/// <summary> * ���𱣴汸��¼�Ķ��� </summary>
					/// 
					class Caretaker
					{
					///	
					///	 <summary> * ��¼������ı���¼���� </summary>
					///	 
					private:
						Memento *memento;
					///	
					///	 <summary> * ���汸��¼���� </summary>
					///	 * <param name="memento"> ������ı���¼���� </param>
					///	 
					public:
						virtual void saveMemento(Memento *memento);
					///	
					///	 <summary> * ��ȡ������ı���¼���� </summary>
					///	 * <returns> ������ı���¼���� </returns>
					///	 
						virtual Memento *retriveMemento();
					};


				}
			}
		}
	}
}
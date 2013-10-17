#pragma once

#include "FlowAMockMemento.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace memento
			{
				namespace example3
				{

					///
					/// <summary> * �������ļ��б���ģ����������A�Ķ���ı���¼���� </summary>
					/// 
					class FlowAMementoFileCareTaker
					{

					///	
					///	 <summary> * ���汸��¼���� </summary>
					///	 * <param name="memento"> ������ı���¼���� </param>
					///	 
					public:
						virtual void saveMemento(FlowAMockMemento *memento);
					///	
					///	 <summary> * ��ȡ������ı���¼���� </summary>
					///	 * <returns> ������ı���¼���� </returns>
					///	 
						virtual FlowAMockMemento *retriveMemento();
					};

				}
			}
		}
	}
}
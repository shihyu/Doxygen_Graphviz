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
				namespace example4
				{

					///
					/// <summary> * ��������Ľӿ� </summary>
					/// 
					class OperationApi
					{
					///	
					///	 <summary> * ��ȡ������ɺ�Ľ�� </summary>
					///	 * <returns> ������ɺ�Ľ�� </returns>
					///	 
					public:
						virtual public int getResult() = 0;
					///	
					///	 <summary> * ִ�мӷ� </summary>
					///	 * <param name="num"> ��Ҫ�ӵ��� </param>
					///	 
						virtual public void add(int) = 0;
					///	
					///	 <summary> * ִ�м��� </summary>
					///	 * <param name="num"> ��Ҫ������ </param>
					///	 
						virtual public void substract(int) = 0;

					///	
					///	 <summary> * ��������ԭ���������״̬�ı���¼���� </summary>
					///	 * <returns> �����õı���¼���� </returns>
					///	 
						virtual public Memento *createMemento() = 0;
					///	
					///	 <summary> * ��������ԭ���������״̬������ص�����¼�����¼��״̬ </summary>
					///	 * <param name="memento"> ��¼��ԭ����״̬�ı���¼���� </param>
					///	 
						virtual public void setMemento(Memento*) = 0;
					};

				}
			}
		}
	}
}
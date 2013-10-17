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
					/// <summary> * ����һ������Ľӿ� </summary>
					/// 
					class Command
					{
					///	
					///	 <summary> * ִ������ </summary>
					///	 
					public:
						virtual public void execute() = 0;
					///	
					///	 <summary> * ��������ָ�������¼�����¼��״̬ </summary>
					///	 * <param name="m"> ����¼���� </param>
					///	 
						virtual public void undo(Memento*) = 0;
					///	
					///	 <summary> * ��������ָ�������¼�����¼��״̬ </summary>
					///	 * <param name="m"> ����¼���� </param>
					///	 
						virtual public void redo(Memento*) = 0;
					///	
					///	 <summary> * ��������ԭ���������״̬�ı���¼���� </summary>
					///	 * <returns> �����õı���¼���� </returns>
					///	 
						virtual public Memento *createMemento() = 0;
					};

				}
			}
		}
	}
}
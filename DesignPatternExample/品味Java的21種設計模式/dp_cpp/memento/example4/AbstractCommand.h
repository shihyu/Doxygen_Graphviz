#pragma once

#include "Command.h"
#include "OperationApi.h"
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
					/// <summary> * �������Ĺ�������ʵ�ָ����������Ĺ������� </summary>
					/// 
					class AbstractCommand : public Command
					{
					///	
					///	 <summary> * ����Ĺ���ʵ�֣����ﲻ�� </summary>
					///	 
					public:
						virtual void execute() = 0;
					///	
					///	 <summary> * ��������������ʵ���߶��� </summary>
					///	 
					protected:
						OperationApi *operation;
					public:
						virtual void setOperation(OperationApi *operation);

						virtual Memento *createMemento();

						virtual void redo(Memento *m);

						virtual void undo(Memento *m);
					};

				}
			}
		}
	}
}
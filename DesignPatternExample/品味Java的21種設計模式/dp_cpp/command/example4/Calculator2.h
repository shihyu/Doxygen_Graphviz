#pragma once

#include "Command.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace command
			{
				namespace example4
				{
					///
					/// <summary> * �������࣬���������мӷ���ť��������ť </summary>
					/// 
					class Calculator2
					{
					///	
					///	 <summary> * ����ִ�мӷ���������� </summary>
					///	 
					private:
						Command *addCmd;
					///	
					///	 <summary> * ����ִ�м������������ </summary>
					///	 
						Command *substractCmd;
					///	
					///	 <summary> * ����ִ�мӷ���������� </summary>
					///	 * <param name="addCmd"> ִ�мӷ���������� </param>
					///	 
					public:
						virtual void setAddCmd(Command *addCmd);
					///	
					///	 <summary> * ����ִ�м������������ </summary>
					///	 * <param name="substractCmd"> ִ�м������������ </param>
					///	 
						virtual void setSubstractCmd(Command *substractCmd);
					///	
					///	 <summary> * �ṩ���ͻ�ʹ�ã�ִ�мӷ����� </summary>
					///	 
						virtual void addPressed();
					///	
					///	 <summary> * �ṩ���ͻ�ʹ�ã�ִ�м������� </summary>
					///	 
						virtual void substractPressed();
					};

				}
			}
		}
	}
}
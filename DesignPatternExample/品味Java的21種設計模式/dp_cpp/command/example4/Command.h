#pragma once

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
					/// <summary> * ����ӿڣ�����ִ�еĲ�����֧�ֿɳ������� </summary>
					/// 
					class Command
					{
					///	
					///	 <summary> * ִ�������Ӧ�Ĳ��� </summary>
					///	 
					public:
						virtual public void execute() = 0;
					///	
					///	 <summary> * ִ�г��������Ӧ�Ĳ��� </summary>
					///	 
						virtual public void undo() = 0;
					};

				}
			}
		}
	}
}